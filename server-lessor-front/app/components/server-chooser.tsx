'use client'
import "./chooser.css";
import {useEffect, useState} from "react";
import {GetAdminServers, GetUsers, User} from "@/api/admin";
import {useUserStore} from "@/context/user-store";
import {Server} from "@/api/config";

export function ServerChooser({setServerAction, style}: { setServerAction: ({ip, id}: { ip: string, id: string }) => void, style?: React.CSSProperties }) {
    const [servers, setServers] = useState<Server[]>([]);
    const [username, setIp] = useState('');
    const inited = useUserStore((state) => state.inited);

    const [focused, setFocused] = useState(false);

    useEffect(() => {
        if (inited) GetAdminServers().then(setServers);
    }, [inited]);

    useEffect(() => {
        if(!username) setServerAction({ip: '', id: ''});
    }, [username]);

    return (
        <div className="chooser" style={style}>
            <input type="text" onFocus={() => setFocused(true)} placeholder="Адресс сервера..."
                   value={username} onBlur={() => setTimeout(() => setFocused(false), 100)}
                   onChange={e => setIp(e.target.value)}/>
            <div style={{position: 'relative', width: '100%'}}>
                <div style={{position: 'absolute', top: '0', left: 0}} className="chooser__list">
                    {focused && servers.filter(e => e.ip.startsWith(username)).map(u =>
                        <div key={u.id}
                             onClick={() => {
                                 setServerAction(u);
                                 setIp(u.ip);
                             }}>{u.ip}
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}