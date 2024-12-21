import axios, {AxiosError} from "axios";
import {BACK_URL} from "@/api/auth";
import {useUserStore} from "@/context/user-store";

export interface Invite {
    referralUsername: string
    status: string
}

export async function GetInvited(): Promise<Invite[]> {
    try {
        const res = await axios.get(BACK_URL + '/invitations', {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return res.data;
    } catch (e: any) {
        const message = ((e as AxiosError)?.response?.data as string) || (e as AxiosError)?.message;
        return [];
    }
}

