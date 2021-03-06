package com.w11k.lsql.cli.tests.stmts1;

import com.w11k.lsql.cli.tests.structural_fields.*;
import java.util.*;

@SuppressWarnings({"Duplicates", "WeakerAccess"})
public final class LoadAllPersonsEscaped2 implements com.w11k.lsql.TableRow, Theid_Integer {

    // static methods ----------

    @SuppressWarnings("unchecked")
    public static <T extends 
            Theid_Integer> LoadAllPersonsEscaped2 from(T source) {
        Object target = new LoadAllPersonsEscaped2();
        target = ((Theid_Integer) target).withTheid(source.getTheid());
        return (LoadAllPersonsEscaped2) target;
    }

    @SuppressWarnings("unused")
    public static LoadAllPersonsEscaped2 fromInternalMap(java.util.Map<String, Object> internalMap) {
        return new LoadAllPersonsEscaped2((java.lang.Integer) internalMap.get("theid"));
    }

    @SuppressWarnings("unused")
    public static LoadAllPersonsEscaped2 fromMap(java.util.Map<String, Object> map) {
        return new LoadAllPersonsEscaped2((java.lang.Integer) map.get("theid"));
    }

    // constructors ----------

    @SuppressWarnings("ConstantConditions")
    public LoadAllPersonsEscaped2() {
        this.theid = null;
    }

    @SuppressWarnings("NullableProblems")
    private LoadAllPersonsEscaped2(
            java.lang.Integer theid) {
        this.theid = theid;
    }

    // fields ----------

    @SuppressWarnings("unused")
    public static final String INTERNAL_FIELD_THEID = "theid";

    @SuppressWarnings("unused")
    public static final String FIELD_THEID = "theid";

    @javax.annotation.Nonnull public final java.lang.Integer theid;

    @javax.annotation.Nonnull public java.lang.Integer getTheid() {
        return this.theid;
    }

    public LoadAllPersonsEscaped2 withTheid(@javax.annotation.Nonnull java.lang.Integer theid) {
        return new LoadAllPersonsEscaped2(theid);
    }

    // class methods ----------

    @SuppressWarnings("unchecked")
    public <T extends 
            Theid_Integer> T as(T targetStart) {
        Object target = targetStart;
        target = ((Theid_Integer) target).withTheid(this.getTheid());
        return (T) target;
    }

    @SuppressWarnings("unchecked")
    public <T extends 
            Theid_Integer> T as(Class<? extends T> targetClass) {
        try {
            Object target = targetClass.newInstance();
            return this.as((T) target);
        } catch (Exception e) {throw new RuntimeException(e);}
    }

    public java.util.Map<String, Object> toInternalMap() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("theid", this.theid);
        return map;
    }

    public java.util.Map<String, Object> toMap() {
        java.util.Map<String, Object> map = new java.util.HashMap<>();
        map.put("theid", this.theid);
        return map;
    }

    // Object methods ----------

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoadAllPersonsEscaped2 that = (LoadAllPersonsEscaped2) o;
        return     Objects.equals(theid, that.theid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(theid);
    }

    @Override
    public String toString() {
        return "LoadAllPersonsEscaped2{" + "theid=" + theid + "}";
    }

}
