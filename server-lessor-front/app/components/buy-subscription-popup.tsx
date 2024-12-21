'use client'
import React, {useEffect, useState} from "react";
import useAnimatedRouter from "@/app/components/use-animated-router";
import {useUserStore} from "@/context/user-store";


export default function BuySubscriptionPopup() {
    const dialog = React.createRef<HTMLDialogElement>();
    const {animatedRoute} = useAnimatedRouter();
    const inited = useUserStore((state) => state.inited);
    const subscription = useUserStore((state) => state.subscription);

    useEffect(() => {
        console.log(subscription, inited)
        if (!subscription && inited) {
            dialog.current?.showModal();
            dialog.current?.addEventListener('close', () => animatedRoute('/'));
        }
    }, [inited, subscription]);

    return (
        <>
            <dialog style={{maxHeight: '80vh', overflowY: 'auto'}} ref={dialog}>
                <form method="dialog">
                    <h1>Для просмотра и создания конфигов требуется активная подписка</h1>
                    <button style={{marginTop: '40px'}}>
                        За подпиской
                    </button>
                </form>
            </dialog>
        </>
    )
}