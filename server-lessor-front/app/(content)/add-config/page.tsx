'use client'
import styles from "./page.module.css";
import {GetConfigLocations, GetConfigProtocols, Location, Protocol} from "@/api/config";
import Configurer from "@/app/(content)/add-config/configurer";
import {useEffect, useState} from "react";
import {useUserStore} from "@/context/user-store";

export default function Home() {
    const [locations, setLocations] = useState<Location[]>([]);
    const [protocols, setProtocols] = useState<Protocol[]>([]);

    const inited = useUserStore((state) => state.inited);

    useEffect(() => {
        if(!inited) return;
        GetConfigLocations().then(setLocations);
        GetConfigProtocols().then(setProtocols);
    }, [inited]);

    console.log(locations, protocols)

    return (
        <main className={styles.main}>
            <h1 className={styles.title}>
                Подберите сервер для себя
            </h1>
            <Configurer locations={locations} protocols={protocols}/>
        </main>
    );
}
