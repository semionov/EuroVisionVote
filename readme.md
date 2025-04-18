# Eurovision Vote System

In the last edition of Eurovision, the organization was the victim of a series of cyberattacks aimed at cutting off
communications between countries to prevent voting.

It is because of these facts that it has been decided that for the next edition a new system of votes has to be
developed.

The new system needs to be secure and guarantee the privacy of those votes. So, the best technology that achieve these
requirements is a [Blockchain](https://builtin.com/blockchain), that provides a decentralized database and ensures the
immutability of the data.

## How it works?

The aim of this REST API is to add and retrieve the votes between countries and show the current state of these votes.
To do so, we are going to build an API that allow the countries to submit a vote to another country and also retrieve
the votes submitted from all countries.

To provide a good security layer, all the countries will be attached to that Blockchain network (with a P2P network)
sharing the transactions. This will give us an extra security layer against any kind of cyberattack but also
traceability of what's happening.

So, when the voting endpoint is called, the API will store the vote as a transaction and then generates a new block that
will be added to the blockchain.

Finally, the node will update the Blockchain to all nodes that are connected to the P2P network.

> To make this more simple, in these Eurovision edition every country can only vote **one time** and to **one country**.

Here you could find the [OpenAPI description file](/api.spec.yaml) with the requirements of the API.

### Workflow

The workflow of this API is as follows:

1. Vote request is received
2. Check that the given `originCountryCode` has not already voted (you could find the list of countries in
   the [OpenAPI spec file](api.spec.yaml))
    1. If it has been used we return an error
    2. If it has not been used, we save the vote as a transaction and generates a new block to add to the blockchain
3. Add the new Block to the Blockchain
4. Update the Blockchain in all the active nodes

## Technical Considerations

Keep in mind the following:

1. There is no need to manage parallel update of the Blockchain in several nodes at the same time, it will be updated
   only in one at the same time.
2. Related with the Blockchain:
    1. The blockchain have a first block called 'genesis block', these block doesn't contain a reference of the previous
       block, usually is hardcoded on the software
    2. Every Block has multiple properties:
        1. `timestamp`: the timestamp for the moment when the block was created
        2. `lastHash`: hash of the previous block on the Blockchain
        3. `data`: information we want to store in the block (in our case the relation between the country and the
           votes)
        4. `hash`: a SHA256 string for the block, calculated concatenating the timestamp, lastHash and data.
    3. The implementation of the Blockchain must follow these contract:
    ```
    interface Blockchain {
      /** Adds new block to the blockchain */
      addBlock(block: Block): Block
      /**
       * Validates the chain by checking if:
       * - every element's last hash value matches previous block's hash
       * - data has not been tampered (which will produce a different hash value)
       * - genesis block's hash values match
       */
      isValid(blockchain: Blockchain): boolean
      /** The new blockchain that is a candidate for replacing the current blockchain */
      replace(blockchain: Blockchain): boolean
    }
    
    interface Block {
      /** Generate the first block for the chain */
      static getGenesisBlock(): Block
      /** Generate the hash for the given block */
      static generateHashFromBlock(block: Block): string
    }
    ```

3. The P2P network is a set of servers interconnected through **websocket connections**, several servers could be
   interconnected.

## Technical requirements

* Create a **clean**, **maintainable** and **well-designed** code. We expect to see a good and clear architecture that
  allows to add or modify the solution without so much troubles.
* **Test** your code until you are comfortable with it. We don't expect a 100% of Code Coverage but some tests that
  helps to have a more stable and confident base code.

To understand how you take decisions during the implementation, **please write a COMMENTS.md** file explaining some of
the most important parts of the application.

---

## How to submit your solution

* Push your code to the `devel` branch - we encourage you to commit regularly to show your thinking process was.
* **Create a new Pull Request** to `main` branch & **merge it**.

Once merged you **won't be able to change or add** anything to your solution, so double-check that everything is as
you expected!

Remember that **there is no countdown**, so take your time and implement a solution that you are proud!

--- 

<p align="center">
  If you have any feedback or problem, let us know! 🤘
</p>
