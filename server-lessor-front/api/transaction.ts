import {Subscription, useUserStore} from "@/context/user-store";
import {BACK_URL} from "@/api/auth";
import axios, {AxiosError} from "axios";


export async function GetSubscription(): Promise<Subscription | null> {
    try {
        const res = await axios.get(BACK_URL + '/subscriptions', {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return res.data
    } catch (e) {
        console.warn(e);
        if ((e as AxiosError).status == 403)
            useUserStore.getState().Logout();
        return null;
    }
}

export interface Transaction {
    id: string
    amount: number
    creationTime: number
    updateTime: number
    status: string
    tariffName: string
    paymentUrl: string | null
}

export async function GetTransactions(): Promise<Transaction[]>{
    try {
        const res = await axios.get(BACK_URL + '/transactions', {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return res.data
    } catch (e) {
        console.warn(e);
        if ((e as AxiosError).status == 403)
            useUserStore.getState().Logout();
        return [];
    }
}

export async function GetTransaction(id: string): Promise<Transaction | null>{
    try {
        const res = await axios.get(BACK_URL + '/transactions/' + id, {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return res.data
    } catch (e) {
        console.warn(e);
        if ((e as AxiosError).status == 403)
            useUserStore.getState().Logout();
        return null;
    }
}