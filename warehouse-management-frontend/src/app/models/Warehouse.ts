

export class Warehouse {
    id: string; // is UUID in DB 
    name: string;
    location: string;
    max_capacity: number;
    created_at: string; //LocalDateTime in DB 

    constructor(
        id: string,
        name: string,
        location: string,
        max_capacity: number,
        created_at: string
    ) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.max_capacity = max_capacity;
        this.created_at = created_at;
    }

    //Formatting the date
    public formatCreatedAt(): string {
        const date = new Date(this.created_at);
        return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
    }

}
