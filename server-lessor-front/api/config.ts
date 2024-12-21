import axios, {AxiosError} from "axios";
import {BACK_URL, Message} from "@/api/auth";
import {useUserStore} from "@/context/user-store";


export enum ProtocolType {
    SSH, FTP, HTTP, HTTPS
}

export interface Protocol {
    id: string
    type: ProtocolType
    port: number
}

export interface Config {
    id: string
    serverLogin: string
    protocol: Protocol
    serverIp: string
    userId: string
    serverId: string
    name: string
}

export async function GetConfigs(): Promise<Config[]> {
    try {
        const res = await axios.get(BACK_URL + '/configurations', {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return res.data;
    } catch (e: any) {
        const message = ((e as AxiosError)?.response?.data as string) || (e as AxiosError)?.message;
        return [];
    }
}

export async function CreateConfig(name: string, protocol: ProtocolType, country: string): Promise<Message> {
    try {
        const res = await axios.post(BACK_URL + '/configurations', {
            name,
            protocolType: protocol,
            country
        }, {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return {isError: false, message: JSON.stringify(res.data)};
    } catch (e: any) {
        const message: string | {message: string} = ((e as AxiosError)?.response?.data as string) || (e as AxiosError)?.message;
        // @ts-ignore
        return {isError: true, message: message.message || message};
    }
}

export interface Location {
    id: string
    country: string
    city?: string
}

export interface Server {
    id: string
    location: Location
    ip: string
    protocols: Protocol[]
    rootLogin?: string
    rootPassword?: string
}

export async function DeleteConfig(id: string): Promise<Message>{
    try {
        const res = await axios.delete(BACK_URL + '/configurations/' + id, {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return {isError: true, message: res.data};
    } catch (e: any) {
        const message = ((e as AxiosError)?.response?.data as string) || (e as AxiosError)?.message;
        return {isError: true, message: message};
    }
}

export async function GetConfigProtocols(): Promise<Protocol[]> {
    try {
        const res = await axios.get(BACK_URL + '/locations', {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return res.data;
    } catch (e: any) {
        const message = ((e as AxiosError)?.response?.data as string) || (e as AxiosError)?.message;
        return [];
    }
}

export async function GetConfigLocations(): Promise<Location[]> {
    try {
        const res = await axios.get(BACK_URL + '/protocols', {headers: {Authorization: 'Bearer ' + useUserStore.getState().token}})
        return res.data;
    } catch (e: any) {
        const message = ((e as AxiosError)?.response?.data as string) || (e as AxiosError)?.message;
        return [];
    }
}

