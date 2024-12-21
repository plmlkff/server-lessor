'use client'
import styles from "@/app/(content)/configs/page.module.css";
import BuySubscriptionPopup from "@/app/components/buy-subscription-popup";
import {useEffect, useState} from "react";
import {Server} from "@/api/config";
import {useUserStore} from "@/context/user-store";
import ServerViewer from "@/app/(content)/servers/viewer";
import {GetAdminServers} from "@/api/admin";


export default function Servers(){
    const [servers, setServers] = useState<Server[]>([]);
    const inited = useUserStore((state) => state.inited);

    useEffect(() => {
        if(!inited) return;
        GetAdminServers().then(setServers);
    }, [inited]);

    return (
        <main className={styles.main}>
            <BuySubscriptionPopup/>
            <div className={styles.header}>
                <h1 className={styles.title}>
                    Сервера
                </h1>
            </div>
            <ServerViewer servers={servers}/>
        </main>
    )
}