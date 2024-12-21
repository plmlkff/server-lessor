'use client'
import styles from "./page.module.css";
import {GetTariff, GetTariffs, Tariff} from "@/api/tariff";
import {useEffect, useState} from "react";
import {useUserStore} from "@/context/user-store";
import useAnimatedRouter from "@/app/components/use-animated-router";

export default function Home() {
    const [tariffs, setTariffs] = useState<Tariff[]>([]);
    const {animatedRoute} = useAnimatedRouter();

    const inited = useUserStore((state) => state.inited);

    useEffect(() => {
        if (inited) GetTariffs().then(setTariffs);
    }, [inited]);

    return (
        <main className={styles.main}>
            <h1 className={styles.title}>
                Выберите свой план
            </h1>
            <div className={styles.tariffs} style={{gridTemplateColumns: "1fr ".repeat(tariffs.length)}}>
                {tariffs.map((tariff) => (
                    <div key={tariff.id}>
                        <h3 className={styles.tariff_name}>{tariff.name}</h3>
                        <div className={styles.tariff_price}><p>₽</p>{tariff.price}
                            <div>руб/<br/>месяц</div>
                        </div>
                        <p className={styles.tariff_configs}>{declineSettings(tariff.configCount)}</p>
                        <button className={styles.purchase} onClick={() =>
                            GetTariff(tariff.id)
                                .then(res => {
                                    if (!res.isError) animatedRoute(res.message);
                                })}>Оформить
                        </button>
                    </div>
                ))}
            </div>
        </main>
    );
}

function declineSettings(count: number): string {
    const forms = ['настройка', 'настройки', 'настроек'];
    const remainder = count % 10;
    const remainderHundred = count % 100;

    if (remainder === 1 && remainderHundred !== 11) {
        return `${count} ${forms[0]}`; // 1 настройка
    } else if (remainder >= 2 && remainder <= 4 && (remainderHundred < 12 || remainderHundred > 14)) {
        return `${count} ${forms[1]}`; // 2 настройки, 3 настройки, 4 настройки
    } else {
        return `${count} ${forms[2]}`; // 0, 5-9, 11-14 настройки
    }
}
