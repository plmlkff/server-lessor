import axios, {AxiosError} from "axios";
import {useUserStore} from "@/context/user-store";
import {User} from "@/api/admin";

// export const BACK_URL = 'http://localhost:9090/api';
export const BACK_URL = '/api';


export interface Message {
    message: string;
    isError: boolean;
}

export async function Login(login: string, password: string): Promise<Message> {
    try {
        const res = await axios.post(BACK_URL + '/users/auth', {
            login: login,
            password: password
        })
        useUserStore.getState().Login(login, res.data.accessToken, (res.data.roles as string[]).includes("ADMIN"), res.data.referralCode);
        return {isError: false, message: JSON.stringify(res.data)};
    } catch (e: any) {
        const message: string | {message: string} = ((e as AxiosError)?.response?.data as string) || (e as AxiosError)?.message;
        // @ts-ignore
        return {isError: true, message: message.message || message};
    }
}

export async function SignUp(login: string, password: string, referral: number): Promise<Message> {
    try {
        const res = await axios.post(BACK_URL + '/users/signup', {
            login: login,
            password: password,
            refCode: referral
        })
        useUserStore.getState().Login(login, res.data.accessToken, (res.data.roles as string[]).includes("ADMIN"), res.data.referralCode);
        return {isError: false, message: JSON.stringify(res.data)};
    } catch (e: any) {
        const message: string | {message: string} = ((e as AxiosError)?.response?.data as string) || (e as AxiosError)?.message;
        // @ts-ignore
        return {isError: true, message: message.message || message};
    }
}