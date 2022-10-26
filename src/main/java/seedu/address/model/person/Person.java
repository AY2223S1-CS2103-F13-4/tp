package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import seedu.address.model.category.Category;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated,
 * immutable.
 */
public class Person {

    // Identity fields
    private final Uid uid;
    private final Name name;
    private final Gender gender;
    private final Phone phone;
    private final Email email;

    // Data fields
    private final Address address;
    private final Set<Tag> tags = new HashSet<>();

    // Check fields
    private final int similarityThreshold = 5;

    /**
     * Every field must be present and not null.
     */
    public Person(Uid uid, Name name, Gender gender, Phone phone, Email email, Address address, Set<Tag> tags) {
        requireAllNonNull(name, phone, email, address, tags);
        if (Objects.isNull(uid)) {
            this.uid = new Uid();
        } else {
            this.uid = uid;
        }
        this.name = name;
        this.gender = gender;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.tags.addAll(tags);
    }

    /**
     * Building Pattern constructor of all the subclasses of Person
     *
     * @param category
     * @param id
     * @param name
     * @param gender
     * @param phone
     * @param email
     * @param address
     * @param tagList
     * @param dateTimeList
     * @param visitStatus
     * @return
     */
    public static Person of(Category category, Uid id, Name name, Gender gender, Phone phone, Email email,
            Address address, Set<Tag> tagList, List<DateTime> dateTimeList, Optional<VisitStatus> visitStatus) {
        if (category.isNurse()) {
            return new Nurse(id, name, gender, phone, email, address, tagList);
        }
        return new Patient(id, name, gender, phone, email, address, tagList, dateTimeList, visitStatus);
    }

    /**
     * @return the id
     */
    public Uid getUid() {
        return uid;
    }

    public Name getName() {
        return name;
    }

    public Gender getGender() {
        return gender;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    public Address getAddress() {
        return address;
    }

    public Category getCategory() {
        return null;
    }

    public String getCategoryIndicator() {
        return "person";
    }

    /**
     * Returns an immutable tag set, which throws
     * {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person otherPerson) {
        if (otherPerson == this) {
            return true;
        }

        return otherPerson != null && otherPerson.getUid().equals(getUid());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;

        return otherPerson.getUid().equals(getUid())
                && otherPerson.getName().equals(getName())
                && otherPerson.getGender().equals(getGender())
                && otherPerson.getPhone().equals(getPhone())
                && otherPerson.getEmail().equals(getEmail())
                && otherPerson.getAddress().equals(getAddress())
                && otherPerson.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(uid, name, gender, phone, email, address, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Uid: ")
                .append(getUid())
                .append("; Name: ")
                .append(getName())
                .append("; Gender: ")
                .append(getGender())
                .append("; Phone: ")
                .append(getPhone())
                .append("; Email: ")
                .append(getEmail())
                .append("; Address: ")
                .append(getAddress());

        Set<Tag> tags = getTags();
        if (!tags.isEmpty()) {
            builder.append("; Tags: ");
            tags.forEach(builder::append);
        }
        return builder.toString();
    }

    /**
     * Returns true if both persons are similar to each other.
     */
    public boolean isSimilarPerson(Person otherPerson) {
        int counter = 0;
        if (otherPerson == this) {
            return true;
        }
        if (otherPerson.getName().equals(getName())) {
            counter++;
        }
        if (otherPerson.getGender().equals(getGender())) {
            counter++;
        }
        if (otherPerson.getPhone().equals(getPhone())) {
            counter++;
        }
        if (otherPerson.getEmail().equals(getEmail())) {
            counter++;
        }
        if (otherPerson.getAddress().equals(getAddress())) {
            counter++;
        }
        if (otherPerson.getTags().equals(getTags())) {
            counter++;
        }
        return counter >= similarityThreshold;
    }
}
