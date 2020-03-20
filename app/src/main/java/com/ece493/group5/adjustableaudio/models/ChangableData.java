package com.ece493.group5.adjustableaudio.models;

import java.util.EnumSet;
import java.util.Observable;
import java.util.Observer;

public class ChangableData<T extends Enum<T>>
        extends Observable
{
    private Class<T> clazz;

    protected EnumSet<T> changed;

    protected ChangableData(Class<T> clazz)
    {
        this.clazz = clazz;
        this.changed = EnumSet.noneOf(this.clazz);
    }

    public void setChanged(T type, boolean value)
    {
        setChanged();
        if (value) {
            changed.add(type);
        } else {
            changed.remove(type);
        }
    }

    public boolean getChanged(T type)
    {
        return changed.contains(type);
    }

    public void setAllChanges()
    {
        setChanged();
        changed = EnumSet.allOf(clazz);
    }

    public void clearAllChanges()
    {
        changed = EnumSet.noneOf(clazz);
        clearChanged();
    }
}
