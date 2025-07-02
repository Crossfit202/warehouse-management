export class Warehouse {
    id: string;
    name: string;
    location: string;
    max_capacity: number;

    constructor(data: {
        id: string,
        name: string,
        location: string,
        max_capacity: number,
    }) {
        this.id = data.id;
        this.name = data.name;
        this.location = data.location;
        this.max_capacity = data.max_capacity;
    }
}
