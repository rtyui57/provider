import { MessageBuilder } from "../shared/message";

const messageBuilder = new MessageBuilder();
const logger = Logger.getLogger("SIDE_APP");

// Simulating an asynchronous network request using Promise
const mockAPI = async () => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      resolve({
        body: {
          data: {
            text: "HELLO ZEPPOS",
          },
        },
      });
    }, 1000);
  });
};

const fetchData = async (ctx) => {
  try {
    logger.log("Hacer la peticion")
    // Requesting network data using the fetch API
    // The sample program is for simulation only and does not request real network data, so it is commented here
    // Example of a GET method request
     const ress = await fetch({
       url: 'http://192.168.0.15:8080/mock/greetings',
       method: 'GET'
     })
    // Example of a POST method request
    // const res = await fetch({
    //   url: 'https://xxx.com/api/xxx',
    //   method: 'POST',
    //   headers: {
    //     'Content-Type': 'application/json'
    //   },
    //   body: JSON.stringify({
    //     text: 'Hello Zepp OS'
    //   })
    // })

    // A network request is simulated here
    //const res = await mockAPI()
    const resBody = typeof res.body === 'string' ?  JSON.parse(res.body) : res.body

    ctx.response({
      data: { result: resBody.data },
    })

  } catch (error) {
    ctx.response({
      data: { result: "ERROR" },
    });
  }
};

AppSideService({
  onInit() {
    messageBuilder.listen(() => {});

    messageBuilder.on("request", (ctx) => {
      const jsonRpc = messageBuilder.buf2Json(ctx.request.payload);
      if (jsonRpc.method === "GET_DATA") {
        return fetchData(ctx);
      }
    });
  },

  onRun() {},

  onDestroy() {},
});
