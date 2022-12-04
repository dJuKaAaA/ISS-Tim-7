package com.tim7.iss.tim7iss.models;

public class Enums {

    public enum MessageType{
        SUPPORT,
        RIDE,
        PANIC,
    }

    public enum PaymentType{
        CARD,
        PAYPAL,
        BITCOIN,
        CASH,
    }

    public enum RideStatus{
        PENDING,
        ACCEPTED,
        REJECTED,
        ACTIVE,
        FINISHED,
        CANCELED,
    }

    public enum VehicleName{
        STANDARD,
        LUXURIOUS,
        VAN,
    }

    public enum Day{
        MON,
        TUE,
        WED,
        THU,
        FRI,
        SAT,
        SUN,
    }
}
