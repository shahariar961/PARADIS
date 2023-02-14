-module(gen_worker).
-compile(export_all).



start(InitialState,Handler) ->
    spawn(fun() -> loop(InitialState,Handler) end).

call(S,Request) ->
    Ref=make_ref(),
    S ! {self(),Ref,Request},
    receive
        {Ref,Response} -> Response
end.

loop(State,Handler) ->
    receive 
        {From,Ref,Request} ->
            case Handler(State,Request) ->
                {reply,NewState,Response} ->
                    From ! {Ref,Response},
                    loop(NewState,Handler)
                end
            end.