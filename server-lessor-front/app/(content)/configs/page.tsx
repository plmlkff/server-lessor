'use client'
import styles from "./page.module.css";
import {Config, GetConfigs} from "@/api/config";
import ConfigViewer from "@/app/(content)/configs/viewer";
import Link from "next/link";
import {useEffect, useState} from "react";
import {useUserStore} from "@/context/user-store";
import BuySubscriptionPopup from "@/app/components/buy-subscription-popup";
import {UserChooser} from "@/app/components/user-chooser";
import {GetAdminConfigs} from "@/api/admin";
import {ServerChooser} from "@/app/components/server-chooser";

export default function Home() {
    const [configs, setConfigs] = useState<Config[]>([]);
    const inited = useUserStore((state) => state.inited);
    const isAdmin = useUserStore(state => state.isAdmin);

    const [anyUser, setAnyUser] = useState<{login: string, id: string}>({
        login: '',
        id: ''
    });
    const [anyServer, setAnyServer] = useState<{ip: string, id: string}>({
        ip: '',
        id: ''
    });

    useEffect(() => {
        if(!inited) return;
        setConfigs([]);
        if(anyUser.id || anyServer.id){
            GetAdminConfigs(anyUser.id, anyServer.id).then(setConfigs);
        }else {
            GetConfigs().then(setConfigs);
        }
    }, [inited, anyUser, anyServer]);

    return (
        <main className={styles.main}>
            <BuySubscriptionPopup/>
            <div className={styles.header}>
                {isAdmin && <UserChooser setUserAction={setAnyUser}/>}
                <h1 className={styles.title}>
                    {anyUser.login ? "Конфигурации " + anyUser.login : "Ваши конфигурации"}
                </h1>
            </div>
            <div className={styles.header}>
                {isAdmin && <ServerChooser setServerAction={setAnyServer} style={{position: 'static', marginLeft: '3%'}}/>}
                <Link href={'/add-config'} className={styles.add_config}>
                    <button>Добавить конфигурацию</button>
                </Link>
            </div>
                <ConfigViewer configs={configs}/>
        </main>
);
}
