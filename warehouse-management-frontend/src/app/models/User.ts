

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
        if (!this.created_at) return 'N/A';

        // Replace the space with 'T' and trim microseconds to milliseconds
        let cleaned = this.created_at.replace(' ', 'T');
        cleaned = cleaned.split('.')[0]; // remove microseconds

        const date = new Date(cleaned);
        if (isNaN(date.getTime())) {
            console.warn('Invalid date:', this.created_at);
            return 'Invalid Date';
        }

        return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
    }



}
