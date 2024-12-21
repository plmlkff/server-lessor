import {BACK_URL} from "@/api/auth";
import axios, {AxiosError} from "axios";
import {Subscription, useUserStore} from "@/context/user-store";
import {Transaction} from "@/api/transaction";
import {Config, Server} from "@/api/config";

export interface User {
    id: string
    login: string
    referralCode: number
    role: string
    accessToken: string
}

export async function GetUsers(){
    try {
        const res = await axios.get(BACK_URL + '/admin/users', {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return res.data;
    } catch (e: any) {
        const message = ((e as AxiosError)?.response?.data as string) || (e as AxiosError)?.message;
        return [];
    }
}

export async function GetAdminTransactions(id: string): Promise<Transaction[]>{
    try {
        const res = await axios.get(BACK_URL + '/admin/transactions?userId='+id, {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return res.data
    } catch (e) {
        console.warn(e);
        if ((e as AxiosError).status == 403)
            useUserStore.getState().Logout();
        return [];
    }
}


export async function GetAdminSubscription(id: string): Promise<Subscription | null> {
    try {
        const res = await axios.get(BACK_URL + '/admin/subscriptions?userId='+id, {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return res.data
    } catch (e) {
        console.warn(e);
        if ((e as AxiosError).status == 403)
            useUserStore.getState().Logout();
        return null;
    }
}

export async function GetAdminConfigs(id: string, server: string): Promise<Config[]>{
    try {
        const res = await axios.get(BACK_URL + '/admin/configurations?userId='+id + (server ? '&serverId=' + server : ''), {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return res.data
    } catch (e) {
        console.warn(e);
        if ((e as AxiosError).status == 403)
            useUserStore.getState().Logout();
        return [];
    }
}



export async function GetAdminServers(): Promise<Server[]> {
    try {
        const res = await axios.get(BACK_URL + '/admin/servers', {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return res.data;
    } catch (e: any) {
        const message = ((e as AxiosError)?.response?.data as string) || (e as AxiosError)?.message;
        return [];
    }
}