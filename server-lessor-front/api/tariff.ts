import axios, {AxiosError} from "axios";
import {BACK_URL, Message} from "@/api/auth";
import {useUserStore} from "@/context/user-store";

export interface Tariff {
    id: string
    name: string
    price: number
    configCount: number;
}

export async function GetTariffs(): Promise<Tariff[]>{
    try {
        const res = await axios.get(BACK_URL + '/tariffs', {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return res.data;
    } catch (e: any) {
        const message = ((e as AxiosError)?.response?.data as string) || (e as AxiosError)?.message;
        return [];
    }
}

export async function GetTariff(id: string): Promise<Message> {
    try {
        const res = await axios.post(BACK_URL + '/transactions', {tariffId: id}, {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return {isError: false, message: res.data.paymentUrl};
    } catch (e: any) {
        const message = ((e as AxiosError)?.response?.data as string) || (e as AxiosError)?.message;
        // @ts-ignore
        return {isError: true, message: message.message || message};
    }
}