package seedu.address.logic.commands;

import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Optional;
import java.util.function.Predicate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.category.Category;
import seedu.address.model.person.Address;
import seedu.address.model.person.Gender;
import seedu.address.model.person.Nurse;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * ListCommand.
 */
public class ListCommandTest {

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_noFiltersApplied_showsEverything() {
        Command listCommand = new ListCommand(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        assertCommandSuccess(listCommand,
                model, String.format(ListCommand.MESSAGE_SUCCESS, "NIL", "NIL", "NIL", "NIL", "NIL", "NIL"),
                expectedModel);
    }

    @Test
    public void execute_addressFilterApplied_showsJurongUsers() {
        Predicate<Person> predicate = x -> x.getAddress().value.contains("Jurong");
        expectedModel.updateFilteredPersonList(predicate);

        Command listCommand = new ListCommand(Optional.of(new Address("Jurong")), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        assertCommandSuccess(listCommand,
                model, String.format(ListCommand.MESSAGE_SUCCESS, "Jurong", "NIL", "NIL", "NIL", "NIL", "NIL"),
                expectedModel);
    }

    @Test
    public void execute_categoryFilterApplied_showsPatients() {
        Predicate<Person> predicate = x -> x.getCategory().equals(new Category(Category.PATIENT_SYMBOL));
        expectedModel.updateFilteredPersonList(predicate);

        Command listCommand = new ListCommand(Optional.empty(), Optional.of(new Category(Category.PATIENT_SYMBOL)),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty());
        assertCommandSuccess(listCommand,
                model, String.format(ListCommand.MESSAGE_SUCCESS, "NIL", "P", "NIL", "NIL", "NIL", "NIL"),
                expectedModel);
    }

    @Test
    public void execute_genderFilterApplied_showsMaleUsers() {
        Predicate<Person> predicate = x -> x.getGender().equals(new Gender(Gender.MALE_SYMBOL));
        expectedModel.updateFilteredPersonList(predicate);

        Command listCommand = new ListCommand(Optional.empty(), Optional.empty(),
                Optional.of(new Gender(Gender.MALE_SYMBOL)), Optional.empty(), Optional.empty(),
                Optional.empty());
        assertCommandSuccess(listCommand,
                model, String.format(ListCommand.MESSAGE_SUCCESS, "NIL", "NIL", "M", "NIL", "NIL", "NIL"),
                expectedModel);
    }

    @Test
    public void execute_tagFilterApplied_showsFriendsTag() {
        Predicate<Person> predicate = x -> x.getTags().stream().anyMatch(y -> y.tagName == "friends");
        expectedModel.updateFilteredPersonList(predicate);

        Command listCommand = new ListCommand(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.of(new Tag("friends")), Optional.empty(), Optional.empty());
        assertCommandSuccess(listCommand,
                model, String.format(ListCommand.MESSAGE_SUCCESS, "NIL", "NIL", "NIL", "friends", "NIL", "NIL"),
                expectedModel);
    }

    @Test
    public void execute_fullyAssignedFilterApplied_showsTrueTag() {
        Predicate<Person> predicate = x -> {
            boolean dateSlotMatch;
            if (x.getCategory().toString().equals("P")) {
                Patient p = ((Patient) x);
                dateSlotMatch = p.hasBeenFullyAssigned();
            } else {
                Nurse n = ((Nurse) x);
                dateSlotMatch = n.getUnavailableDates().size()
                        + n.getFullyScheduledDates().size()
                        >= 7;
            }
            return dateSlotMatch;
        };
        expectedModel.updateFilteredPersonList(predicate);

        Command listCommand = new ListCommand(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.of(Boolean.TRUE), Optional.empty());
        assertCommandSuccess(listCommand,
                model, String.format(ListCommand.MESSAGE_SUCCESS, "NIL", "NIL", "NIL", "NIL", "true", "NIL"),
                expectedModel);
    }

    @Test
    public void execute_notFullyVisitedFilterApplied_showsFalseTag() {
        Predicate<Person> predicate = x -> {
            boolean homeVisitMatch;
            if (x.getCategory().toString().equals("P")) {
                Patient p = ((Patient) x);
                homeVisitMatch = p.hasBeenFullyVisited();
            } else {
                Nurse n = ((Nurse) x);
                homeVisitMatch = n.hasCompletedAllVisits();
            }
            return !homeVisitMatch;
        };
        expectedModel.updateFilteredPersonList(predicate);

        Command listCommand = new ListCommand(Optional.empty(), Optional.empty(),
                Optional.empty(), Optional.empty(), Optional.empty(), Optional.of(Boolean.FALSE));
        assertCommandSuccess(listCommand,
                model, String.format(ListCommand.MESSAGE_SUCCESS, "NIL", "NIL", "NIL", "NIL", "NIL", "false"),
                expectedModel);
    }

    @Test
    public void execute_allFiltersApplied_showsAlice() {
        Predicate<Person> predicate = x -> {
            boolean addressMatch = x.getAddress().value.contains("Jurong");
            boolean categoryMatch = x.getCategory().categoryName.equals(Category.PATIENT_SYMBOL);
            boolean genderMatch = x.getGender().gender.equals(Gender.FEMALE_SYMBOL);
            boolean tagMatch = x.getTags().stream().anyMatch(y -> y.tagName == "friends");
            return addressMatch && categoryMatch && genderMatch && tagMatch;
        };
        expectedModel.updateFilteredPersonList(predicate);

        Command listCommand = new ListCommand(Optional.of(new Address("Jurong")),
                Optional.of(new Category(Category.PATIENT_SYMBOL)),
                Optional.of(new Gender(Gender.FEMALE_SYMBOL)),
                Optional.of(new Tag("friends")),
                Optional.empty(),
                Optional.empty());
        assertCommandSuccess(listCommand,
                model, String.format(ListCommand.MESSAGE_SUCCESS, "Jurong", "P", "F", "friends", "NIL", "NIL"),
                expectedModel);
    }
}
