package fr.traqueur.endiapower.api;

import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.UUID;

public interface IManager {

      void createPower(IPower power);

      void removePower(int id) throws NoSuchElementException;

      IPower getPower(int id) throws NoSuchElementException;

      IPower getPowerByName(String name) throws NoSuchElementException;

      Set<IPower> getPowers();

      HashMap<IPower, Integer> getPlayerPowers(UUID uuid);

      boolean hasPower(UUID uuid, int id);

      void grantPower(UUID uuid, IPower power, int level);

      void revokePower(UUID uuid, IPower power);

      void loadData();

      void saveData();

}
