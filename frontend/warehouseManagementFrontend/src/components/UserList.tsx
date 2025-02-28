import { useEffect, useState } from "react";
import { getAllUsers, deleteUser } from "../services/userService";

interface User {
    id: string;
    name: string;
    email: string;
}

function UserList() {
    const [users, setUsers] = useState<User[]>([]);

    useEffect(() => {
        fetchUsers();
    }, []);

    const fetchUsers = async () => {
        const data = await getAllUsers();
        setUsers(data);
    };

    const handleDelete = async (userId: string) => {
        const success = await deleteUser(userId);
        if (success) {
            setUsers(users.filter((user) => user.id !== userId));
        }
    };

    return (
        <div>
            <h2>User List</h2>
            <ul>
                {users.map((user) => (
                    <li key={user.id}>
                        {user.name} - {user.email}
                        <button onClick={() => handleDelete(user.id)}>Delete</button>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default UserList;
