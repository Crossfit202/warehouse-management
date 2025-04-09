import { Warehouse } from "./Warehouse";

export class Alert {
    id: string;
    warehouse: Warehouse
    message: string;
    status: string;
    time: string;

    constructor(
        id: string,
        warehouse: Warehouse,
        message: string,
        status: string,
        time: string
    ) {
        this.id = id;
        this.warehouse = warehouse;
        this.message = message;
        this.status = status;
        this.time = time;
    }

    public formatTime(): string {
        if (!this.time) return 'N/A';

        let cleaned = this.time.replace(' ', 'T');
        cleaned = cleaned.split('.')[0]; // remove microseconds

        const date = new Date(cleaned);
        if (isNaN(date.getTime())) {
            console.warn('Invalid date:', this.time);
            return 'Invalid Date';
        }

        return `${date.toLocaleDateString()} ${date.toLocaleTimeString()}`;
    }

}