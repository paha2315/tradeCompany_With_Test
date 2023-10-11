package trade_company.views.fictive_table_objects;

import trade_company.logic.sql_object.Availability;

public class AvailabilityCount {
    Availability availability;
    int count;

    public AvailabilityCount(Availability availability) {
        setAvailability(availability);
        setCount(0);
    }

    public AvailabilityCount(Availability availability, int count) {
        setAvailability(availability);
        setCount(count);
    }

    public AvailabilityCount(AvailabilityCount availability) {
        setAvailability(availability.getAvailability());
        setCount(availability.getCount());
    }

    public Availability getAvailability() {
        return availability;
    }

    public void setAvailability(Availability availability) {
        this.availability = availability;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
