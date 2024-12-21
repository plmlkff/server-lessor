'use client'
import Spinner from "@/app/components/spinner";
import {useEffect, useState} from "react";
import styles from "./../login/page.module.css";
import {Message, SignUp} from "@/api/auth";
import MessageComponent from "@/app/components/message";
import Link from "next/link";
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
		if(response && !response.isError){

		}
	}, [response, animatedRoute]);

	useEffect(() => {
		if(user.login) animatedRoute("/");
	}, [user, animatedRoute]);

	console.log(response)

	return (
		<main style={{height: '-webkit-fill-available', display: 'flex', flexDirection: "column"}}>
			<h1 className={styles.title}>Регистрация</h1>
			<form className={styles.form} onSubmit={e => {
				e.preventDefault();
				const formData = new FormData(e.currentTarget);

				setResponse(null);
				setRequestSent(true);
				SignUp(formData.get("username") as string, formData.get("password") as string, +(formData.get("referral") || 0))
					.then(setResponse);
			}}>
				<label>Логин<br/><input type="text" name="username" required minLength={2}
										autoComplete="email" onInput={EmailValidator}/></label>
				<label>Пароль<br/><input type="password" name="password" required
										 autoComplete="new-password"/></label>
				<label>Реферальный код<br/><input type="text" name="referral"/></label>
				<MessageComponent message={response}/>
				<div className={styles.buttons}>
					<button disabled={requestSent}>{requestSent &&
						<Spinner size={30} style={{margin: "-11px 0 -11px -32px", paddingRight: "32px"}}/>}Зарегистрироваться
					</button>
				</div>
				<Link href="/login" className={styles.register_link} onClick={(e) => {
					animatedRoute(e.currentTarget.href);
				}}>Вход</Link>
			</form>
		</main>
	)
}
