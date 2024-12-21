'use client'
import styles from "./page.module.css";
import {Protocol, ProtocolType, Server, Location, CreateConfig} from "@/api/config";
import {useEffect, useState} from "react";
import MessageComponent from "@/app/components/message";
import Spinner from "@/app/components/spinner";
import {Message} from "@/api/auth";

export default function Configurer({locations, protocols}: { locations: Location[], protocols: Protocol[] }) {
    const [response, setResponse] = useState<Message | null>(null);
    const [requestSent, setRequestSent] = useState<boolean>(false);

    const [name, setName] = useState<string>('');
    const [location, setLocation] = useState<{ country?: number, city?: string }>({});
    const [protocol, setProtocol] = useState<number>(-1);

    // const locations: { [key: string]: { [key: string]: { [key: number]: string } } } = {};
    // servers.forEach(e => {
    //     if (!locations[e.location.country]) locations[e.location.country] = {};
    //     const city = e.location.city || "Любой";
    //     if (!locations[e.location.country][city]) locations[e.location.country][city] = {};
    //     e.protocols.forEach(p => {
    //         locations[e.location.country][city][p.type] = ProtocolType[p.type];
    //     })
    // });

    console.log(protocol);

    useEffect(() => {
        if (response) setRequestSent(false);
    }, [response]);

    return (
        <div className={styles.configurer}>
            <label>
                Название<br/>
                <input disabled={requestSent} required value={name}
                       onChange={e => setName(e.currentTarget.value)}/>
            </label>
            <div>Страна<br/>
                <select disabled={requestSent}
                        required defaultValue=""
                        onChange={e => {
                            setLocation({country: +e.currentTarget.value});
                        }}>
                    <option value="" disabled>Выберите страну...</option>
                    {locations.map((e, i) => {
                        return <option key={e.country}
                                       value={i}>{e.country}</option>
                    })}
                </select>
            </div>
            <div>Протокол<br/>
                <select
                    value={protocol} required disabled={location.country == undefined || requestSent}
                    onChange={e => {
                        setProtocol(+e.currentTarget.value);
                    }}>
                    <option value={-1} disabled>Выберите протокол...</option>
                    {location.country != undefined && protocols
                        .map((e, i) =>
                            <option key={i} value={i}>{e.type}</option>
                        )}
                </select>
            </div>
            <MessageComponent message={response} onlyError={true}/>
            <div className={styles.buttons}>
                <button disabled={requestSent || protocol < 0 || !name}
                        onClick={() => {
                            console.log(location, protocol)
                            if (location.country == undefined || protocol < 0) return;

                            setRequestSent(true);
                            setResponse(null);

                            CreateConfig(name, protocols[protocol].type, locations[location.country].country).then(setResponse);
                        }}>{requestSent &&
                    <Spinner size={36} style={{margin: "-11px 0 -11px -32px", paddingRight: "32px"}}/>}Заказать
                </button>
            </div>
        </div>
    )
}