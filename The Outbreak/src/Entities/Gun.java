package Entities;

public class Gun {
    final private int capacity;
    public int ammo, totalAmmo;
    final private int damage;
    final private int firingSpeed;

    public Gun(int capacity, int damage, int firingSpeed) {
        this.capacity = capacity;
        this.damage = damage;
        this.firingSpeed = firingSpeed;

        ammo = capacity;
        totalAmmo = 50;
    }

    public void reload() {
        // reload ammo
        if (capacity - ammo <= totalAmmo) {
            totalAmmo -= capacity - ammo;
            ammo = capacity;
        }
        else {
            ammo += totalAmmo;
            totalAmmo = 0;
        }
    }

    public void addAmmo(int ammo) {
        totalAmmo += ammo;

        // max total ammo is 200
        if (totalAmmo > 200) {
            totalAmmo = 200;
        }
    }

    public int getCapacity() {
        return capacity;
    }

    public int getDamage() {
        return damage;
    }

    public int getFiringSpeed() {
        return firingSpeed;
    }

    public int getAmmo() {
        return ammo;
    }
}
