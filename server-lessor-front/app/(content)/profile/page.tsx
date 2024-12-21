'use client'

import {Subscription, useUserStore} from "@/context/user-store";
import styles from "./page.module.css";
import Link from "next/link";
import {GetTransactions, Transaction} from "@/api/transaction";
import {useEffect, useState} from "react";
import {UserChooser} from "@/app/components/user-chooser";
import {GetAdminSubscription, GetAdminTransactions} from "@/api/admin";
import {GetInvited, Invite} from "@/api/invites";

export default function Profile() {
    const user = useUserStore(state => state.login);
    const isAdmin = useUserStore(state => state.isAdmin);
    const [transactions, setTransactions] = useState<Transaction[] | null>(null);
    const inited = useUserStore((state) => state.inited);
    const [subscription, setSubscription] = useState<Subscription | null>(null);

    const [invites, setInvites] = useState<Invite[]>([]);

    const [anyUser, setAnyUser] = useState<{ login: string, id: string }>({
        login: '',
        id: ''
    });

    useEffect(() => {
        if (!inited) return;
        setTransactions([]);
        setSubscription(null);
        if (anyUser.id) {
            GetAdminTransactions(anyUser.id).then(setTransactions);
            GetAdminSubscription(anyUser.id).then(setSubscription);
        } else {
            GetTransactions().then(setTransactions);
            useUserStore.getState().UpdateSubscription().then(setSubscription);
        }
    }, [inited, anyUser]);

    useEffect(() => {
        if (!inited) return;
        GetInvited().then(setInvites);
    }, [inited]);

    return (
        <main className={styles.main}>
            <div className={styles.header}>
                {isAdmin && <UserChooser setUserAction={setAnyUser}/>}
                <h1 className={styles.title}>
                    {anyUser.login ? "Профиль пользователя " + anyUser.login : "Ваш профиль" + (user ? ", " + user : "") }
                </h1>
                <Link href='/login' className={styles.logout}>
                    <button onClick={() => useUserStore.getState().Logout()}>Выйти</button>
                </Link>
            </div>
            <div className={styles.content}>
                <div>
                    <h1 className={styles.section_title}>
                        {!transactions ? "Ваши операции загружаются..." : "Ваши операции"}
                    </h1>
                    {transactions && (transactions.length > 0 ? <div className={styles.transaction_table}>
                        <table>
                            <thead>
                            <tr>
                                <th>Статус</th>
                                <th>Тариф</th>
                                <th>Стоимость</th>
                                <th>Последнее обновление</th>
                                <th>Дата создания</th>
                            </tr>
                            </thead>
                            <tbody>
                            {transactions.map(t =>
                                <tr key={t.id}>
                                    <td>{t.paymentUrl ? <a href={t.paymentUrl}>{t.status}</a> : t.status}</td>
                                    <td>{t.tariffName}</td>
                                    <td>{t.amount}</td>
                                    <td>{new Date(t.updateTime).toLocaleDateString()}</td>
                                    <td>{new Date(t.creationTime).toLocaleDateString()}</td>
                                </tr>)}
                            </tbody>
                        </table>
                    </div> : <p style={{textAlign: "center", fontSize: "24px", marginTop: "20px"}}>отсутствуют</p>)}
                </div>
                <div>
                    <h1 className={styles.section_title}>
                        Ваш рефералльный код
                    </h1>
                    <p style={{textAlign: "center", fontSize: "24px", marginTop: "20px"}}>{useUserStore.getState().ref_code}</p>
                </div>
                <div>
                    <h1 className={styles.section_title}>
                        Ваши реферралы
                    </h1>
                    <div className={styles.transaction_table}>
                        <table>
                            <thead>
                            <tr>
                                <th>Реферрал</th>
                                <th>Статус</th>
                            </tr>
                            </thead>
                            <tbody>
                            {invites && invites.map(inv =>
                                <tr key={inv.referralUsername}>
                                    <td>{inv.referralUsername}</td>
                                    <td>{inv.status}</td>
                                </tr>)}
                            </tbody>
                        </table>
                    </div>
                </div>
                <div>
                    <h1 className={styles.section_title}>
                        {subscription ? "Активная подписка" : "Подписка не активна"}
                    </h1>
                    {subscription ? <div className={styles.subscription}>
                        <h3>Тариф: </h3>        <h3>{subscription.tariffName}</h3>
                        <p>Дата окончания: </p> <p>{new Date(subscription.expirationTime).toLocaleDateString()}</p>
                        <p>Дата покупки: </p>   <p>{new Date(subscription.creationTime).toLocaleDateString()}</p>
                    </div> : <></>
                    }
                </div>
            </div>
        </main>
    )
}