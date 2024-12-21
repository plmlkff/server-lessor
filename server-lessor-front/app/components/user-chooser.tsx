'use client'
import "./chooser.css";
import {useEffect, useState} from "react";
import {GetUsers, User} from "@/api/admin";
import {useUserStore} from "@/context/user-store";

export function UserChooser({setUserAction}: { setUserAction: ({login, id}: { login: string, id: string }) => void }) {
    const [users, setUsers] = useState<User[]>([]);
    const [username, setUsername] = useState('');
    const inited = useUserStore((state) => state.inited);

    const [focused, setFocused] = useState(false);

    useEffect(() => {
        if (inited) GetUsers().then(setUsers);
    }, [inited]);

    useEffect(() => {
        if(!username) setUserAction({login: '', id: ''});
    }, [username]);

    return (
        <div className="chooser">
            <input type="text" onFocus={() => setFocused(true)} placeholder="Профиль рользователя..."
                   value={username} onBlur={() => setTimeout(() => setFocused(false), 100)}
                   onChange={e => setUsername(e.target.value)}/>
            <div style={{position: 'relative', width: '100%'}}>
                <div style={{position: 'absolute', top: '0', left: 0}} className="chooser__list">
                    {focused && users.filter(e => e.login.startsWith(username)).map(u =>
                        <div key={u.id}
                             onClick={() => {
                                 setUserAction(u);
                                 setUsername(u.login);
                             }}>{u.login}
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}