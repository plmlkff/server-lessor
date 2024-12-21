'use client'
import {Config, DeleteConfig, ProtocolType, Server} from "@/api/config";
import styles from "@/app/(content)/configs/page.module.css";


export default function ServerViewer({servers}: { servers: Server[] }) {
    return (
        <div className={styles.configs_grid}>
            {servers.map((config) => (
                <div className={styles.card} key={config.id}>
                    <h3>Страна: </h3><h3>{config.location.country}</h3>
                    <p>Город: </p><p>{config.location.city}</p>
                    <p>IP: </p><p>{config.ip}</p>
                    <p>Протоколы: </p><p>{config.protocols.map(e => e.type + ":" + e.port).join(", ")}</p>
                    <p>Логин: </p><p>{config.rootLogin}</p>
                    <p>Пароль: </p><p>{config.rootPassword}</p>
                </div>
            ))}
        </div>
    )
}