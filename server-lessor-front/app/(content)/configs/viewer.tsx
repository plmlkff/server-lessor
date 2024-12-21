'use client'
import {Config, DeleteConfig, ProtocolType} from "@/api/config";
import styles from "@/app/(content)/configs/page.module.css";


export default function ConfigViewer({configs}: { configs: Config[] }) {
    return (
        <div className={styles.configs_grid}>
            {configs.map((config) => (
                <div className={styles.card} key={config.id}>
                    <h3>Название: </h3><h3>{config.name}</h3>
                    <p>Логин: </p><p>{config.serverLogin}</p>
                    <p>IP: </p><p>{config.serverIp}</p>
                    <p>Протокол: </p><p>{config.protocol.type}</p>
                    <p>Порт: </p><p>{config.protocol.port}</p>
                    <button className={styles.delete} onClick={e => {
                        DeleteConfig(config.id);
                        e.currentTarget.parentElement?.remove();
                    }}>Удалить
                    </button>
                </div>
            ))}
        </div>
    )
}