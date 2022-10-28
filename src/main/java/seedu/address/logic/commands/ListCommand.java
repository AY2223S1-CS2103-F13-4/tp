package seedu.address.logic.commands;

import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.model.Model;
import seedu.address.model.category.Category;
import seedu.address.model.person.Address;
import seedu.address.model.person.Gender;
import seedu.address.model.person.Nurse;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    public static final String MESSAGE_ARGUMENTS = "ADDRESS: %s, CATEGORY: %s, GENDER: %s, TAG: %s, " +
            "FULLY ASSIGNED: %s, FULLY VISITED: %s";

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons with specifications:\n" + MESSAGE_ARGUMENTS;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all enrolled users who fit the specified criteria, "
            + "or all enrolled users if no criteria were specified.\n"
            + "Parameters: \n"
            + "<optional> c/ [CATEGORY]\n"
            + "<optional> g/ [GENDER]\n"
            + "<optional> a/ [ADDRESS]\n"
            + "<optional> t/ [TAG]\n"
            + "<optional> as/ [FULLY ASSIGNED]\n"
            + "<optional> v/ [FULLY VISITED]\n"
            + "Example: " + COMMAND_WORD + "  "
            + "c/ nurse";

    private final Optional<Address> address;
    private final Optional<Category> category;
    private final Optional<Gender> gender;
    private final Optional<Tag> tag;
    private final Optional<Boolean> dateSlot;
    private final Optional<Boolean> homeVisit;

    /**
     * @param a address to be filtered.
     * @param c category (nurse/patient) to be filtered.
     * @param g gender to be filtered.
     * @param t tag to be filtered.
     * @param d filter for presence or absence of assignment of dateSlots.
     * @param h filter for dateSlots that have been completed or have yet to be completed.
     */
    public ListCommand(Optional<Address> a, Optional<Category> c, Optional<Gender> g, Optional<Tag> t
            , Optional<Boolean> d, Optional<Boolean> h) {
        address = a;
        category = c;
        gender = g;
        tag = t;
        dateSlot = d;
        homeVisit = h;
    }

    @Override
    public CommandResult execute(Model model) {
        Predicate<Person> predicate = x -> {
            boolean addressMatch = x.getAddress().value.toLowerCase()
                    .contains(address.orElse(x.getAddress()).value.toLowerCase());
            boolean categoryMatch = x.getCategory().equalsIgnoreCase(category.orElse(x.getCategory()));
            boolean genderMatch = x.getGender().equalsIgnoreCase(gender.orElse(x.getGender()));

            boolean tagMatch;
            if (x.getTags().size() == 0) {
                tagMatch = tag.isEmpty();
            } else {
                Predicate<Tag> tagPredicate = y -> {
                    Tag tagToCompare = tag.orElse((Tag) x.getTags().toArray()[0]);
                    return y.equals(tagToCompare);
                };
                tagMatch = x.getTags().stream().anyMatch(tagPredicate);
            }

            boolean dateSlotMatch;
            boolean homeVisitMatch;
            if (x.getCategory().toString().equals("P")) {
                Patient p = ((Patient) x);
                dateSlotMatch = p.hasBeenFullyAssigned()
                        == dateSlot.orElse(p.hasBeenFullyAssigned());
                homeVisitMatch = p.hasBeenFullyVisited()
                        == homeVisit.orElse(p.hasBeenFullyVisited());
            } else {
                Nurse n = ((Nurse) x);
                boolean fully_scheduled = n.getUnavailableDates().size()
                        + n.getFullyScheduledDates().size()
                        >= 7;
                dateSlotMatch = fully_scheduled == dateSlot.orElse(fully_scheduled);
                homeVisitMatch = n.hasCompletedAllVisits()
                        == homeVisit.orElse(n.hasCompletedAllVisits());
            }

            return addressMatch && categoryMatch && genderMatch && tagMatch && dateSlotMatch && homeVisitMatch;
        };
        model.updateFilteredPersonList(predicate);

        final String[] filteredGender = new String[1];
        gender.ifPresentOrElse(x -> filteredGender[0] = x.gender, () -> filteredGender[0] = "NIL");
        final String[] filteredCategory = new String[1];
        category.ifPresentOrElse(x -> filteredCategory[0] = x.categoryName, () -> filteredCategory[0] = "NIL");
        final String[] filteredDateSlot = new String[1];
        dateSlot.ifPresentOrElse(x -> filteredDateSlot[0] = x.toString(), () -> filteredDateSlot[0] = "NIL");
        final String[] filteredHomeVisit = new String[1];
        homeVisit.ifPresentOrElse(x -> filteredHomeVisit[0] = x.toString(), () -> filteredHomeVisit[0] = "NIL");
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                address.orElse(new Address("NIL")).value,
                filteredCategory[0],
                filteredGender[0],
                tag.orElse(new Tag("NIL")).tagName,
                filteredDateSlot[0],
                filteredHomeVisit[0]));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ListCommand)) {
            return false;
        }

        // state check
        ListCommand e = (ListCommand) other;
        return address.equals(e.address)
                && category.equals(e.category)
                && gender.equals(e.gender)
                && tag.equals(e.tag);
    }
}
