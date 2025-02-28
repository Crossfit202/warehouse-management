import axios from "axios";

const API_URL = "http://localhost:8083/users"; // Your backend endpoint

// ✅ Get All Users
export const getAllUsers = async () => {
    try {
        const response = await axios.get(API_URL);
        return response.data;
    } catch (error) {
        console.error("Error fetching users:", error);
        return [];
    }
};

// ✅ Get User by ID
export const getUserById = async (userId: string) => {
    try {
        const response = await axios.get(`${API_URL}/${userId}`);
        return response.data;
    } catch (error) {
        console.error("Error fetching user:", error);
        return null;
    }
};

// ✅ Create New User
export const createUser = async (user: { name: string; email: string }) => {
    try {
        const response = await axios.post(API_URL, user);
        return response.data;
    } catch (error) {
        console.error("Error creating user:", error);
        return null;
    }
};

// ✅ Update User
export const updateUser = async (userId: string, user: { name: string; email: string }) => {
    try {
        const response = await axios.put(`${API_URL}/${userId}`, user);
        return response.data;
    } catch (error) {
        console.error("Error updating user:", error);
        return null;
    }
};

// ✅ Delete User
export const deleteUser = async (userId: string) => {
    try {
        await axios.delete(`${API_URL}/${userId}`);
        return true;
    } catch (error) {
        console.error("Error deleting user:", error);
        return false;
    }
};
