export class StorageLocation {
    id: string; // is UUID in DB 
    name: string;
    max_capacity: number;

    constructor(
        id: string,
        name: string,
        max_capacity: number,
    ) {
        this.id = id;
        this.name = name;
        this.max_capacity = max_capacity;
    }

}