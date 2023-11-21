package com.sovereigncraft.mcnpubreg;

import java.util.HashMap;
import java.util.Map;

public class NostrData {
    private Map<String, String> names;

    public NostrData() {
        this.names = new HashMap<>();
    }

    public Map<String, String> getNames() {
        return names;
    }

    public void setNames(Map<String, String> names) {
        this.names = names;
    }

    public void addName(String name, String npub) {
        this.names.put(name, npub);
    }
}
