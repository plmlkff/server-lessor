'use client'
import Spinner from "@/app/components/spinner";
import {useEffect, useState} from "react";
import styles from "./page.module.css";
import {Message} from "@/api/auth";
import MessageComponent from "@/app/components/message";
import {Login} from "@/api/auth";
import Link from "next/link";
// import arrow from "../../public/login/arrow_up.svg"
import {useUserStore} from "@/context/user-store";
import useAnimatedRouter from "@/app/components/use-animated-router";
import {EmailValidator} from "@/app/(auth)/login/validators";


export default function LoginPage() {
	const [response, setResponse] = useState<Message | null>(null)
	const [requestSent, setRequestSent] = useState<boolean>(false);
	const {animatedRoute} = useAnimatedRouter();
	const user = useUserStore((state) => state);

	useEffect(() => {
		if(response) setRequestSent(false);
	}, [response, animatedRoute]);

	useEffect(() => {
		if(user.login) animatedRoute("/");
	}, [response, animatedRoute]);

	return (
		<main style={{height: '-webkit-fill-available', display: 'flex', flexDirection: "column"}}>
			<h1 className={styles.title}>Авторизация</h1>
			<form className={styles.form} onSubmit={e => {
				e.preventDefault();
				const formData = new FormData(e.currentTarget);

				setResponse(null);
				setRequestSent(true);
				Login(formData.get("username") as string, formData.get("password") as string)
					.then(setResponse);
			}}>
				<label>Логин<br/><input type="text" name="username" required minLength={2}
										autoComplete="email" onInput={EmailValidator}/></label>
				<label>Пароль<br/><input type="password" name="password" required
										 autoComplete="current-password"/></label>
				<MessageComponent message={response} onlyError={true}/>
				<div className={styles.buttons}>
					<button disabled={requestSent}>{requestSent &&
						<Spinner size={30} style={{margin: "-11px 0 -11px -32px", paddingRight: "32px"}}/>}Войти
					</button>
				</div>
				<Link href="/register" className={styles.register_link} onClick={(e) => {
					animatedRoute(e.currentTarget.href);
				}}>Регистрация</Link>
			</form>
		</main>
	)
}
