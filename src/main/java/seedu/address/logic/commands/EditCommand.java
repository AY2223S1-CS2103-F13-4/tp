package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE_AND_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GENDER;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import seedu.address.commons.core.Messages;
import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.DateTime;
import seedu.address.model.person.Email;
import seedu.address.model.person.Gender;
import seedu.address.model.person.Name;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Uid;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing person in the address book.
 */
public class EditCommand extends Command {

    public static final String COMMAND_WORD = "edit";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the patient/nurse identified "
            + "by the unique id number used in the displayed person list."
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: UID (must be a positive integer) "
            + "[" + PREFIX_CATEGORY + "CATEGORY] "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_GENDER + "GENDER] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_EMAIL + "EMAIL] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]...\n"
            + "Date and Time are only applicable to patient.\n"
            + "[" + PREFIX_DATE_AND_TIME + "DATE_AND_TIME] \n"
            + "Example: " + COMMAND_WORD + PREFIX_UID +  " 1 "
            + PREFIX_PHONE + "91234567 "
            + PREFIX_EMAIL + "johndoe@example.com";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Edited %1$s: %2$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_PERSON = "This %1$s already exists in the address book.";

    private final Index index;
    private final EditPersonDescriptor editPersonDescriptor;

    /**
     * @param index                of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        requireNonNull(index);
        requireNonNull(editPersonDescriptor);

        this.index = index;
        this.editPersonDescriptor = new EditPersonDescriptor(editPersonDescriptor);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        //if (index.getZeroBased() >= lastShownList.size()) {
        //    throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        //}

        //Person personToEdit = lastShownList.get(index.getZeroBased());
        //Person editedPerson = createEditedPerson(personToEdit, editPersonDescriptor);
        
        Optional<Person> personToEdit = lastShownList.stream().filter(p -> p.getId() == targetUid).findFirst();
        if (!personToEdit.isPresent()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_UID);
        }
        
        Person confirmedPersonToEdit = personToEdit.get();
        Person editedPerson = createEditedPerson(confirmedPersonToEdit, editPersonDescriptor);
       
       
        if (!confirmedPersonToEdit.isSamePerson(editedPerson) && model.hasPerson(editedPerson)) {
            if (personToEdit instanceof Patient) {
                throw new CommandException(String.format(MESSAGE_DUPLICATE_PERSON, PATIENT_INDICATOR));
            }
            throw new CommandException(String.format(MESSAGE_DUPLICATE_PERSON, PERSON_INDICATOR));
        }

        model.setPerson(confirmedPersonToEdit, editedPerson);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        if (personToEdit instanceof Patient) {
            return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, PATIENT_INDICATOR, editedPerson));
        }
        return new CommandResult(String.format(MESSAGE_EDIT_PERSON_SUCCESS, PERSON_INDICATOR, editedPerson));

    }

    /**
     * Creates and returns a {@code Person} with the details of {@code personToEdit}
     * edited with {@code editPersonDescriptor}.
     */
    private static Person createEditedPerson(Person personToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert personToEdit != null;

        String updatedCategory = editPersonDescriptor.getCategory().orElse(personToEdit.getCategory());
        Uid id = personToEdit.getId();
        Name updatedName = editPersonDescriptor.getName().orElse(personToEdit.getName());
        Gender updatedGender = editPersonDescriptor.getGender().orElse(personToEdit.getGender());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(personToEdit.getPhone());
        Email updatedEmail = editPersonDescriptor.getEmail().orElse(personToEdit.getEmail());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(personToEdit.getAddress());
        Set<Tag> updatedTags = editPersonDescriptor.getTags().orElse(personToEdit.getTags());
        
        if (personToEdit instanceof Patient && updatedCategory.equals("P")) {
            List<DateTime> updatedDateTime = editPersonDescriptor.getDatesTimes()
                    .orElse(((Patient) personToEdit).getDatesTimes());
            return new Patient(id, updatedName, updatedGender, updatedPhone, updatedEmail,
                        updatedAddress, updatedTags, updatedDateTime);
        }

        return new Person(id, updatedName, updatedPhone, updatedEmail, updatedAddress, updatedTags);
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditCommand)) {
            return false;
        }

        // state check
        EditCommand e = (EditCommand) other;
        return index.equals(e.index)
                && editPersonDescriptor.equals(e.editPersonDescriptor);
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will
     * replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        
        private Uid id;
        private String category;
        private Name name;
        private Gender gender;
        private Phone phone;
        private Email email;
        private Address address;
        private Set<Tag> tags;
        private List<DateTime> datesTimes;

        public EditPersonDescriptor() {
        }

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setCategory(toCopy.category);
            setId(toCopy.id);
            setName(toCopy.name);
            setGender(toCopy.gender);
            setPhone(toCopy.phone);
            setEmail(toCopy.email);
            setAddress(toCopy.address);
            setTags(toCopy.tags);
            setDatesTimes(toCopy.datesTimes);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            if (category == "P") {
                return CollectionUtil.isAnyNonNull(category, name, gender, phone, email, address, tags, datesTimes);
            }
            return CollectionUtil.isAnyNonNull(category, name, gender, phone, email, address, tags);
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public Optional<String> getCategory() {
            return Optional.ofNullable(category);
        }

        /**
         * @param id the id to set
         */
        public void setId(Uid id) {
            this.id = id;
        }

        /**
         * @return the id
         */
        public Optional<Uid> getId() {
            return Optional.ofNullable(id);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }
        public void setGender(Gender gender) {
            this.gender = gender;
        }

        public Optional<Gender> getGender() {
            return Optional.ofNullable(gender);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setEmail(Email email) {
            this.email = email;
        }

        public Optional<Email> getEmail() {
            return Optional.ofNullable(email);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        /**
         * Sets {@code tags} to this object's {@code tags}.
         * A defensive copy of {@code tags} is used internally.
         */
        public void setTags(Set<Tag> tags) {
            this.tags = (tags != null) ? new HashSet<>(tags) : null;
        }

        /**
         * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
         * if modification is attempted.
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<Set<Tag>> getTags() {
            return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
        }

        /**
         * Sets {@code datesTimes} to this object's {@code datesTimes}.
         * A defensive copy of {@code datesTimes} is used internally.
         */
        public void setDatesTimes(List<DateTime> datesTimes) {
            this.datesTimes = (datesTimes != null) ? new ArrayList<DateTime>(datesTimes) : null;
        }

        /**
         * Returns a dateTime list
         * Returns {@code Optional#empty()} if {@code tags} is null.
         */
        public Optional<List<DateTime>> getDatesTimes() {
            return (datesTimes != null) ? Optional.of(new ArrayList<DateTime>(datesTimes)) : Optional.empty();
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            // state check
            EditPersonDescriptor e = (EditPersonDescriptor) other;

            return getId().equals(e.getId())
                    && getName().equals(e.getName())
                    && getCategory().equals(e.getCategory())
                    && getGender().equals(e.getGender())
                    && getPhone().equals(e.getPhone())
                    && getEmail().equals(e.getEmail())
                    && getAddress().equals(e.getAddress())
                    && getDatesTimes().equals(e.getDatesTimes())
                    && getTags().equals(e.getTags());
        }
    }
}
