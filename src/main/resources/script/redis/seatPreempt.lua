-- seat:lock:{userId}:{timestamp}
-- user:booked:2

local seatCount = tonumber(ARGV[2])
local maxSeat = tonumber(ARGV[3])
local ttl = tonumber(ARGV[4])

-- 현재 사용자 예약 수
local current = tonumber(redis.call("GET", KEYS[#KEYS] or "0"))

-- 사용자 제한 검사
if current + seatCount > maxSeat then
    return -1
end

-- 좌석 선점 여부 검사
for i = 1, #KEYS - 1 do
    if redis.call("EXISTS", KEYS[i]) == 1 then
        return 0
    end
end

-- 좌석 락 설정
for i = 1, #KEYS - 1 do
    redis.call("SET", KEYS[i], ARGV[1], "EX", ttl)
end

-- 사용자 예약 수 증가 + TTL
redis.call("INCRBY", KEYS[#KEYS], seatCount)
redis.call("EXPIRE", KEYS[#KEYS], ttl)

return 1