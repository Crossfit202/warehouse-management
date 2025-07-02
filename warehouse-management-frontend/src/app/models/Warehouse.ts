export class Warehouse {
    id: string;
    name: string;
    location: string;
    max_capacity: number;
    personnel?: any[]; // <-- Add this line

    constructor(data: {
        id: string,
        name: string,
        location: string,
        max_capacity: number,
        personnel?: any[] // <-- Add this line
    }) {
        this.id = data.id;
        this.name = data.name;
        this.location = data.location;
        this.max_capacity = data.max_capacity;
        this.personnel = data.personnel; // <-- Add this line
    }
}
