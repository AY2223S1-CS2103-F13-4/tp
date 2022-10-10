package seedu.address.logic.commands;

import java.util.Optional;
import java.util.function.Predicate;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Lists all persons in the address book to the user.
 */
public class ListCommand extends Command {

    private final Optional<Address> address;
    private final Optional<Category> category;
    private final Optional<Gender> gender;
    private final Optional<Tag> tag;

    public static final String MESSAGE_ARGUMENTS = "ADDRESS: %s, CATEGORY: %s, GENDER: %s, TAG: %s";

    public static final String COMMAND_WORD = "list";

    public static final String MESSAGE_SUCCESS = "Listed all persons with specifications: " + MESSAGE_ARGUMENTS;

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Lists all enrolled users who fit the specified criteria, "
            + "or all enrolled users if no criteria were specified.\n"
            + "Parameters: \n"
            + "<optional> c/ [CATEGORY]\n"
            + "<optional> g/ [GENDER]\n"
            + "<optional> a/ [ADDRESS]\n"
            + "<optional> t/ [TAG]\n"
            + "Example: " + COMMAND_WORD + "  "
            + "c/ nurse";

    /**
     * @param a address to be filtered
     * @param c category (nurse/patient) to be filtered
     * @param g gender to be filtered
     * @param t tag to be filtered
     */
    public ListCommand(Optional<Address> a, Optional<Category> c, Optional<Gender> g, Optional<Tag> t) {
        address = a;
        category = c;
        gender = g;
        tag = t;
    }

    @Override
    public CommandResult execute(Model model) {
        Predicate<Person> predicate = x -> {
            boolean addressMatch = x.getAddress().equals(address.orElse(x.getAddress()));
            boolean categoryMatch = x.getCategory().equals(category.orElse(x.getCategory()));
            boolean genderMatch = x.getGender().equals(gender.orElse(x.getGender()));

            Predicate<Tag> tagPredicate = y -> {
                Tag tagToCompare = tag.orElse((Tag) x.getTags().toArray()[0]);
                return y.equals(tagToCompare);
            };
            boolean tagMatch = x.getTags().stream().anyMatch(tagPredicate);
            return addressMatch && categoryMatch && genderMatch && tagMatch;
        };
        model.updateFilteredPersonList(predicate);

        final String[] filteredCategory = new String[1];
        category.ifPresentOrElse(x -> filteredCategory[0] = x.value, () -> filteredCategory[0] = "NIL");
        final String[] filteredGender = new String[1];
        gender.ifPresentOrElse(x -> filteredGender[0] = x.value, () -> filteredGender[0] = "NIL");
        return new CommandResult(String.format(MESSAGE_SUCCESS, address.orElse(new Address("NIL")).value,
                filteredCategory[0],
                filteredGender[0],
                tag.orElse(new Tag("NIL")).tagName));
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
