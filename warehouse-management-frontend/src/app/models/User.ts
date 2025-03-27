

export class User {
    id: string; // is UUID in DB 
    username: string;
    email: string;
    password: string;
    role: string;
    created_at: string; //LocalDateTime in DB 
    updated_at: string; //LocalDateTime in DB 

    constructor(
        id: string,
        username: string,
        email: string,
        password: string,
        role: string,
        created_at: string,
        updated_at: string
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.created_at = created_at;
        this.updated_at = updated_at;

    }

    //Formatting the date
    public formatCreatedAt(): string {
        const date = new Date(this.created_at);
        return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
    }

}
