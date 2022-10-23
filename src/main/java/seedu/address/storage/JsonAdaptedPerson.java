package seedu.address.storage;

import static seedu.address.model.category.Category.NURSE_SYMBOL;
import static seedu.address.model.category.Category.PATIENT_SYMBOL;

import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.category.Category;
import seedu.address.model.person.*;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Person}.
 */
class JsonAdaptedPerson {

    public static final String MISSING_FIELD_MESSAGE_FORMAT = "Person's %s field is missing!";

    private final Long uid;
    private final String name;
    private final String category;
    private final String gender;
    private final String phone;
    private final String email;
    private final String address;
    private final List<JsonAdaptedDateTime> dateTimes = new ArrayList<>();
    private final List<JsonAdaptedTag> tagged = new ArrayList<>();
    private final String visitStatus;
    private final String pName;
    private final String pPhone;
    private final String pEmail;

    /**
     * Constructs a {@code JsonAdaptedPerson} with the given person details.
     */
    @JsonCreator
    public JsonAdaptedPerson(@JsonProperty("uid") Long uid, @JsonProperty("name") String name,
            @JsonProperty("category") String category,
            @JsonProperty("gender") String gender, @JsonProperty("phone") String phone,
            @JsonProperty("email") String email, @JsonProperty("address") String address,
            @JsonProperty("dateTimes") List<JsonAdaptedDateTime> dateTime,
            @JsonProperty("tagged") List<JsonAdaptedTag> tagged,
            @JsonProperty("phys name") String pName,
            @JsonProperty("phys phone") String pPhone,
            @JsonProperty("phys email") String pEmail,
            @JsonProperty("visit status") String visitStatus) {
        this.uid = uid;
        this.name = name;
        this.category = category;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;

        this.pName = Objects.requireNonNullElse(pName, "NA");
        this.pPhone = Objects.requireNonNullElse(pPhone, "NA");
        this.pEmail = Objects.requireNonNullElse(pEmail, "NA");

        if (dateTime != null) {
            this.dateTimes.addAll(dateTime);
        }

        if (tagged != null) {
            this.tagged.addAll(tagged);
        }

        this.visitStatus = visitStatus;
    }

    /**
     * Converts a given {@code Person} into this class for Jackson use.
     */
    public JsonAdaptedPerson(Person source) {
        category = source.getCategory().categoryName;
        boolean isPatient = category.equals(PATIENT_SYMBOL);

        if (isPatient) {
            dateTimes.addAll(((Patient) source).getDatesTimes().stream()
                    .map(JsonAdaptedDateTime::new)
                    .collect(Collectors.toList()));
            visitStatus = ((Patient) source).getVisitStatus().getVisitStatusString();

            String[] physNameArr = new String[]{"NA"};
            ((Patient) source).getAttendingPhysician().ifPresent(x -> physNameArr[0] = x.getName().fullName);
            pName = physNameArr[0];
            String[] physEmailArr = new String[]{"NA"};
            ((Patient) source).getAttendingPhysician().ifPresent(x -> physEmailArr[0] = x.getEmail().value);
            pEmail = physEmailArr[0];
            String[] physPhoneArr = new String[]{"NA"};
            ((Patient) source).getAttendingPhysician().ifPresent(x -> physPhoneArr[0] = x.getPhone().value);
            pPhone = physPhoneArr[0];
        } else {
            visitStatus = null;
            pName = "NA";
            pPhone = "NA";
            pEmail = "NA";
        }

        uid = source.getUid().uid;
        name = source.getName().fullName;
        gender = source.getGender().gender;
        phone = source.getPhone().value;
        email = source.getEmail().value;
        address = source.getAddress().value;
        tagged.addAll(source.getTags().stream()
                .map(JsonAdaptedTag::new)
                .collect(Collectors.toList()));
    }

    /**
     * Converts this Jackson-friendly adapted person object into the model's
     * {@code Person} object.
     *
     * @throws IllegalValueException if there were any data constraints violated in
     *                               the adapted person.
     */
    public Person toModelType() throws IllegalValueException {
        final List<Tag> personTags = new ArrayList<>();
        for (JsonAdaptedTag tag : tagged) {
            personTags.add(tag.toModelType());
        }


        final List<DateTime> patientHomeVisitDatesTimes = new ArrayList<>();
        for (JsonAdaptedDateTime dateTime : dateTimes) {
            patientHomeVisitDatesTimes.add(dateTime.toModelType());
        }

        if (uid == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Id.class.getSimpleName()));
        }

        final Uid modelUid = new Uid(uid);

        if (name == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Name.class.getSimpleName()));
        }
        if (!Name.isValidName(name)) {
            throw new IllegalValueException(Name.MESSAGE_CONSTRAINTS);
        }
        final Name modelName = new Name(name);

        if (gender == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Gender.class.getSimpleName()));
        }
        if (!Gender.isValidGender(gender)) {
            throw new IllegalValueException(Gender.MESSAGE_CONSTRAINTS);
        }
        final Gender modelGender = new Gender(gender);

        if (phone == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Phone.class.getSimpleName()));
        }
        if (!Phone.isValidPhone(phone)) {
            throw new IllegalValueException(Phone.MESSAGE_CONSTRAINTS);
        }
        final Phone modelPhone = new Phone(phone);

        if (email == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Email.class.getSimpleName()));
        }
        if (!Email.isValidEmail(email)) {
            throw new IllegalValueException(Email.MESSAGE_CONSTRAINTS);
        }
        final Email modelEmail = new Email(email);

        if (address == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT, Address.class.getSimpleName()));
        }
        if (!Address.isValidAddress(address)) {
            throw new IllegalValueException(Address.MESSAGE_CONSTRAINTS);
        }
        final Address modelAddress = new Address(address);

        final Set<Tag> modelTags = new HashSet<>(personTags);

        final List<DateTime> modelDatesTimes = patientHomeVisitDatesTimes;

        if (category == null) {
            throw new IllegalValueException(String.format(MISSING_FIELD_MESSAGE_FORMAT,
                    Category.class.getSimpleName()));
        }
        if (category.equals(NURSE_SYMBOL)) {
            return new Nurse(modelUid, modelName, modelGender, modelPhone, modelEmail, modelAddress, modelTags);
        } else if (category.equals(PATIENT_SYMBOL)) {
            if (visitStatus == null) {
                throw new IllegalValueException(
                        String.format(MISSING_FIELD_MESSAGE_FORMAT,
                                VisitStatus.class.getSimpleName()));
            }
            if (!VisitStatus.isValidVisitStatus(visitStatus)) {
                throw new IllegalValueException(VisitStatus.MESSAGE_CONSTRAINTS);
            }
            final VisitStatus modelVisitStatus = new VisitStatus(visitStatus);
            return new Patient(modelUid, modelName, modelGender, modelPhone, modelEmail,
                    modelAddress, modelTags, modelDatesTimes, modelVisitStatus);
        } else {
            throw new IllegalValueException(Category.MESSAGE_CONSTRAINTS);
        }

    }

}
