export namespace com.w11k.lsql.cli.tests.schema_public {
    export interface A_Table {
        id: number;
    }
}

export namespace com.w11k.lsql.cli.tests.schema_public {
    export interface Person1 {
        firstName: string;
        id: number;
    }
}

export namespace com.w11k.lsql.cli.tests.schema_public {
    export interface Checks {
        yesno: boolean;
    }
}

export namespace com.w11k.lsql.cli.tests.schema_public {
    export interface Person2 {
        firstName: string;
        id: number;
        age: number;
    }
}

export namespace com.w11k.lsql.cli.tests.stmts1 {
    export interface LoadAllPersonsEscaped2 {
        theid: number;
    }
}

export namespace com.w11k.lsql.cli.tests.stmts1 {
    export interface LoadAllPersons {
        id: number;
        firstName: string;
    }
}

export namespace com.w11k.lsql.cli.tests.stmts1 {
    export interface LoadAllPersonsEscaped1 {
        theid: number;
    }
}

export namespace com.w11k.lsql.cli.tests.stmts1 {
    export interface KeepUnderscoreForCamelCase {
        aField: number;
        afield: string;
    }
}

export namespace com.w11k.lsql.cli.tests.subdir.subsubdir.stmtscamelcase2 {
    export interface LoadPersonsByAgeAndFirstName {
        id: number;
        firstName: string;
        age: number;
    }
}
