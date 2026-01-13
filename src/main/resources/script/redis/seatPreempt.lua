-- seat:lock:{userId}:{timestamp}
-- user:booked:2

-- 좌석 선점 스크립트
-- KEYS: 좌석 키들 (seat:{seatId}) + 마지막에 사용자 예매 수 키 (user:booked:{userId})
-- ARGV[1]: 사용자 아이디 (userId)
-- ARGV[2]: 사용자가 예매할 좌석 개수 (seatCount)
-- ARGV[3]: 사용자 별 최대 예매 개수 (maxSeatCount)
-- ARGV[4]: 키의 TTL 시간 (ttl, 상수)

-- 입력 매개변수 파싱
local userId = ARGV[1]
local seatCount = tonumber(ARGV[2])
local maxSeat = tonumber(ARGV[3])
local ttl = tonumber(ARGV[4])

-- 현재 사용자 예약 수
local current = tonumber(redis.call("GET", KEYS[#KEYS])) or 0

-- 조건 검사 1: 사용자 별 예매 개수 제한 검사
if current + seatCount > maxSeat then
    return -1
end

-- 조건 검사 2: 예매할 좌석이 선점되었는지 검사
for i = 1, #KEYS - 1 do
    if redis.call("EXISTS", KEYS[i]) == 1 then
        return 0  -- 좌석이 이미 선점됨
    end
end

-- 조건 검사 3: 선점이 되어있지 않다면 좌석 선점(lock) 및 TTL 설정
for i = 1, #KEYS - 1 do
    redis.call("SET", KEYS[i], userId, "EX", ttl)
end

-- 사용자 예약 수 증가 + TTL
redis.call("INCRBY", KEYS[#KEYS], seatCount)
redis.call("EXPIRE", KEYS[#KEYS], ttl)

return 1  -- 성공