package jankdb;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TableRepository {
    private final Map<String, Table> tables = new ConcurrentHashMap<>();
    
    public Table getOrCreateTable(String name) {
        return tables.computeIfAbsent(name, Table::new);
    }
    
    public void clearAll() {
        tables.values().forEach(Table::Flush);
        tables.clear();
    }
}