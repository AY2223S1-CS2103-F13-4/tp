package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.person.Nurse;

import static seedu.address.model.person.Gender.MALE_SYMBOL;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";
    private static final String NOT_APPLICABLE = "NA";
    private static final String NURSE_LABEL_TEXT = "(Nurse)";
    private static final String PATIENT_LABEL_TEXT = "(Patient)";
    private static final String NAN_LABEL_TEXT = "(Unassigned)";
    private static final String MALE_GENDER_LABEL_TEXT = "Male";
    private static final String FEMALE_GENDER_LABEL_TEXT = "Female";


    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private FlowPane category;
    @FXML
    private Label uid;
    @FXML
    private Label dateTimes;
    @FXML
    private Label id;
    @FXML
    private Label gender;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label email;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");

        if (person instanceof Nurse) {
            dateTimes.setText(NOT_APPLICABLE);
            category.getChildren().add(new Label(NURSE_LABEL_TEXT));
        } else if (person instanceof Patient) {
            dateTimes.setText(((Patient) person).getDatesTimesInString());
            category.getChildren().add(new Label(PATIENT_LABEL_TEXT));
        } else {
            dateTimes.setText(NOT_APPLICABLE);
            category.getChildren().add(new Label(NAN_LABEL_TEXT));
        }
        name.setText(person.getName().fullName);
        if (person.getGender().gender.equals(MALE_SYMBOL)){
            gender.setText(MALE_GENDER_LABEL_TEXT);
        } else {
            gender.setText(FEMALE_GENDER_LABEL_TEXT);
        }
        uid.setText("UID: [" + person.getUid().toString() + "]");
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        email.setText(person.getEmail().value);
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof PersonCard)) {
            return false;
        }

        // state check
        PersonCard card = (PersonCard) other;
        return id.getText().equals(card.id.getText())
                && person.equals(card.person);
    }
}
