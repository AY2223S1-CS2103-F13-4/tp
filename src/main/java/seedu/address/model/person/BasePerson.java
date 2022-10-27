package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.model.category.Category;

/**
 * Represents a Base Person in the database with only name, phone and email fields.
 */
public abstract class BasePerson {

    private final Name name;
    private final Phone phone;
    private final Email email;

    /**
     * Initialise name, phone and email for person
     * @param n name
     * @param p phone
     * @param e email
     */
    public BasePerson(Name n, Phone p, Email e) {
        requireAllNonNull(n, p, e);
        name = n;
        phone = p;
        email = e;
    }

    public abstract Category getCategory();

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Email getEmail() {
        return email;
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("Name: ")
                .append(getName())
                .append("; Phone: ")
                .append(getPhone())
                .append("; Email: ")
                .append(getEmail());

        return builder.toString();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof BasePerson)) {
            return false;
        }

        BasePerson otherBasePerson = (BasePerson) other;

        return otherBasePerson.getName().equals(getName())
                && otherBasePerson.getPhone().equals(getPhone())
                && otherBasePerson.getEmail().equals(getEmail());
    }
}