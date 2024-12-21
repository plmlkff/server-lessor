'use client'
import styles from "./page.module.css";
import Spinner from "@/app/components/spinner";
import {useEffect} from "react";
import {GetTransaction} from "@/api/transaction";
import useAnimatedRouter from "@/app/components/use-animated-router";

export default function Transaction({params}: { params: Promise<{ code: string }> }) {
    const {animatedRoute} = useAnimatedRouter();

    useEffect(() => {
        const interval =
            setInterval(async () => {
                GetTransaction((await params).code).then(tr => tr && tr.status.toLowerCase() === 'paid' && animatedRoute('/profile'));
            }, 1000);
        return () => clearInterval(interval);
    }, []);

    return (
        <main className={styles.main}>
            <h1 className={styles.title}>
                Ожидание оплаты
            </h1>
            <Spinner size={120}/>
        </main>
    )
}