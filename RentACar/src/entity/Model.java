package entity;

import java.lang.reflect.Type;

public class Model {
    private int id;
    private int brand_id;
    private String name;
    private String year;
    private Type type;
    private Fuel fuel;
    private Gear gear;
    private Brand brand;
    public enum Fuel{
        GASOLINE,
        LPG,
        ELECTRİC,
        DIESEL
    }
    public enum Gear{
        MANUEL,
        AUTO
    }
    public enum Type{
        SEDAN,
        HATCHBACK
    }

    public Model() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrand_id() {
        return brand_id;
    }

    public void setBrand_id(int brand_id) {
        this.brand_id = brand_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public Fuel getFuel() {
        return fuel;
    }

    public void setFuel(Fuel fuel) {
        this.fuel = fuel;
    }

    public Gear getGear() {
        return gear;
    }

    public void setGear(Gear gear) {
        this.gear = gear;
    }

    public Brand getBrand() {
        return brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }
}
