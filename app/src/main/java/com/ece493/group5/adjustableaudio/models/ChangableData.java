package com.ece493.group5.adjustableaudio.models;

import java.util.EnumSet;

public class ChangableData<T extends Enum<T>>
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
        changed = EnumSet.allOf(clazz);
    }

    public void clearAllChanges()
    {
        changed = EnumSet.noneOf(clazz);
    }
}
