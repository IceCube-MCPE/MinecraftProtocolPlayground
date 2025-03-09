# MinecraftProtocolPlayground
Just open source reversed protocol for Minecraft Bedrock Edition (MCPE/MCBE). RakNet and NetherNet implemented
# TODO:
## RakNet
- RU: 
- Рассортировать RakNet пакеты из discovery там, где они находятся. Например GamePacket это не Discovery, он лежит внутри FrameSetPacket в Frame
- Сделать компрессоры, шифраторы для игрового протокола (GamePacket вскрыть)
- Добавить больше пакетов для кодировки (только два пакета в методе Encode)
- EN: 
- Sort RakNet packets from discovery where they are. For example, GamePacket is not Discovery, it is inside FrameSetPacket in Frame
- Make compressors, encoders for the game protocol (open GamePacket)
- Add more packets for encoding (only two packets in the Encode method)

## NetherNet
- RU:
- Добавить поддержку WebRTC, а не только вскрытие сигнал сокета (7551)
- EN:
- Add WebRTC support, not just socket signal cracking (7551)