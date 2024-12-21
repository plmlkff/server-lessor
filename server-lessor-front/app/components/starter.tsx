'use client'
import {useEffect} from "react";
import {useUserStore} from "@/context/user-store";
import {usePathname, useRouter} from "next/navigation";


export default function Starter(){
    const router = useRouter();
    const path = usePathname();

    useEffect(() => {
        useUserStore.getState().init(router, path);
    }, [])

    return <></>
}