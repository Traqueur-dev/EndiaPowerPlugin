package fr.traqueur.endiapower.api;

import java.util.HashMap;
import java.util.UUID;

public interface IUser {

    UUID getUUID();
    HashMap<IPower, Integer> getPowers();
    void grantPower(IPower power, int level);
    void revokePower(IPower power);
}