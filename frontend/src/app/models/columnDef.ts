export interface ColumnDef<T> {
    header: string;
    field: keyof T;
    formatter?: (value: any, item?: T) => string;
    idField?: keyof T;
}
