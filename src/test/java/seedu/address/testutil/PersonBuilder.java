package seedu.address.testutil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import seedu.address.model.category.Category;
import seedu.address.model.person.Address;
import seedu.address.model.person.DateTime;
import seedu.address.model.person.Email;
import seedu.address.model.person.Gender;
import seedu.address.model.person.Name;
import seedu.address.model.person.Nurse;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Uid;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;

/**
 * A utility class to help with building Person objects.
 */
public class PersonBuilder {
    public static final String DEFAULT_CATEGORY = "P";
    public static final String DEFAULT_UID = "100";
    public static final String DEFAULT_NAME = "Amy Bee";
    public static final String DEFAULT_GENDER = "F";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_EMAIL = "amy@gmail.com";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_DATE_AND_TIME = "2022-06-14T13:00";

    private Category category;
    private Uid uid;
    private Name name;
    private Gender gender;
    private Phone phone;
    private Email email;
    private Address address;
    private Set<Tag> tags;
    private List<DateTime> dateTimeList;

    /**
     * Creates a {@code PersonBuilder} with the default details.
     */
    public PersonBuilder() {
        category = new Category(DEFAULT_CATEGORY);
        uid = new Uid(DEFAULT_UID);
        name = new Name(DEFAULT_NAME);
        gender = new Gender(DEFAULT_GENDER);
        phone = new Phone(DEFAULT_PHONE);
        email = new Email(DEFAULT_EMAIL);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
        dateTimeList = new ArrayList<>();
        dateTimeList.add(new DateTime(DEFAULT_DATE_AND_TIME));
    }

    /**
     * Initializes the PersonBuilder with the data of {@code personToCopy}.
     */
    public PersonBuilder(Person personToCopy) {
        category = personToCopy.getCategory();
        uid = personToCopy.getUid();
        name = personToCopy.getName();
        gender = personToCopy.getGender();
        phone = personToCopy.getPhone();
        email = personToCopy.getEmail();
        address = personToCopy.getAddress();
        tags = new HashSet<>(personToCopy.getTags());
        if (personToCopy.getCategory().categoryName.equals("P")) {
            dateTimeList = new ArrayList<>(((Patient) personToCopy).getDatesTimes());
        }
    }

    /**
     * Sets the {@code Id} of the {@code Person} that we are building.
     */
    public PersonBuilder withUid(String id) {
        this.uid = new Uid(id);
        return this;
    }

    /**
     * Sets the {@code Category} of the {@code Person} that we are building.
     */
    public PersonBuilder withCategory(String category) {
        this.category = new Category(category);
        return this;
    }

    /**
     * Sets the {@code Name} of the {@code Person} that we are building.
     */
    public PersonBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Gender} of the {@code Person} that we are building.
     */
    public PersonBuilder withGender(String gender) {
        this.gender = new Gender(gender);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Person} that we are building.
     */
    public PersonBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Person} that we are building.
     */
    public PersonBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Person} that we are building.
     */
    public PersonBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Email} of the {@code Person} that we are building.
     */
    public PersonBuilder withEmail(String email) {
        this.email = new Email(email);
        return this;
    }

    /**
     * Parses the {@code datesTimes} into a {@code Set<DateTime>} and
     * set it to the {@code Person} that we are building.
     * Applies only to Patient.
     */
    public PersonBuilder withDatesTimes(String ... datesTimes) {
        this.dateTimeList = SampleDataUtil.getDatesTimesList(datesTimes);
        return this;
    }

    /**
     * Build a person for test.
     */
    public Person build() {
        if (category.categoryName.equals("N")) {
            return new Nurse(uid, name, gender, phone, email, address, tags);
        } else if (this.category.categoryName.equals("P")) {
            return new Patient(uid, name, gender, phone, email, address, tags, dateTimeList);
        }
        return new Person(uid, name, gender, phone, email, address, tags);

    }

}
